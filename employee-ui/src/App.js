import { useEffect, useRef, useState } from "react";
import "./App.css";
import Header from "./components/Header";
import EmployeeList from "./components/EmployeeList";
import "react-toastify/dist/ReactToastify.css";
import { getEmployees, saveEmployee, updatePhoto } from "./api/EmployeeService";
import { Navigate, Route, Routes } from "react-router-dom";
import EmployeeDetails from "./components/EmployeeDetails";
import { toastError } from "./api/ToastService";
import { ToastContainer } from "react-toastify";

function App() {
  const modalRef = useRef();
  const fileRef = useRef();

  const [data, setData] = useState({});

  const [currentPage, setCurrentPage] = useState(0);

  const [file, setFile] = useState(undefined);

  const [values, setValues] = useState({
    name: "",
    email: "",
    phone: "",
    address: "",
    title: "",
    status: "",
  });

  const getAllEmployees = async (page = 0, size = 8) => {
    try {
      setCurrentPage(page);
      const { data } = await getEmployees(page, size);
      setData(data);
    } catch (error) {
      console.log(error);
      toastError(error.message);
    }
  };

  const onChange = (event) => {
    setValues({ ...values, [event.target.name]: event.target.value });
  };

  const handleNewEmployee = async (event) => {
    event.preventDefault();
    try {
      const { data } = await saveEmployee(values);
      const formData = new FormData();
      formData.append("file", file, file.name);
      formData.append("id", data.id);

      const { data: photoUrl } = await updatePhoto(formData);
      toggleModal(false);
      console.log(photoUrl);
      setFile(undefined);
      fileRef.current.value = null;
      setValues({
        name: "",
        email: "",
        phone: "",
        address: "",
        title: "",
        status: "",
      });
      getAllEmployees();
    } catch (error) {
      console.log(error);
      toastError(error.message);
    }
  };
  const updateEmployee = async (employee) => {
    try {
      const { data } = await saveEmployee(employee);
      console.log(data);
    } catch (error) {
      console.log(error);
    }
  };

  const updateImage = async (formData) => {
    try {
      const { data: photoUrl } = await updatePhoto(formData);
    } catch (error) {
      console.log(error);
      toastError(error.message);
    }
  };

  const toggleModal = (show) =>
    show ? modalRef.current.showModal() : modalRef.current.close();

  useEffect(() => {
    getAllEmployees();
  }, []);
  return (
    <>
      <Header toggleModal={toggleModal} nbOfContacts={data.totalElements} />

      <main className="main">
        <div className="container">
          <Routes>
            <Route path="/" element={<Navigate to={"/employees"} />} />

            <Route
              path="/employees"
              element={
                <EmployeeList
                  data={data}
                  currentPage={currentPage}
                  getAllEmployees={getAllEmployees}
                />
              }
            />
            <Route
              path="/employees/:id"
              element={
                <EmployeeDetails
                  updateEmployee={updateEmployee}
                  updateImage={updateImage}
                />
              }
            />
          </Routes>
        </div>
      </main>

      {/* Modal */}
      <dialog ref={modalRef} className="modal" id="modal">
        <div className="modal__header">
          <h3>New Contact</h3>
          <i onClick={() => toggleModal(false)} className="bi bi-x-lg"></i>
        </div>
        <div className="divider"></div>
        <div className="modal__body">
          <form onSubmit={handleNewEmployee}>
            <div className="user-details">
              <div className="input-box">
                <span className="details">Name</span>
                <input
                  type="text"
                  value={values.name}
                  onChange={onChange}
                  name="name"
                  required
                />
              </div>
              <div className="input-box">
                <span className="details">Email</span>
                <input
                  type="text"
                  value={values.email}
                  onChange={onChange}
                  name="email"
                  required
                />
              </div>
              <div className="input-box">
                <span className="details">Title</span>
                <input
                  type="text"
                  value={values.title}
                  onChange={onChange}
                  name="title"
                  required
                />
              </div>
              <div className="input-box">
                <span className="details">Phone Number</span>
                <input
                  type="text"
                  value={values.phone}
                  onChange={onChange}
                  name="phone"
                  required
                />
              </div>
              <div className="input-box">
                <span className="details">Address</span>
                <input
                  type="text"
                  value={values.address}
                  onChange={onChange}
                  name="address"
                  required
                />
              </div>
              <div className="input-box">
                <span className="details">VISA Status</span>
                <input
                  type="text"
                  value={values.status}
                  onChange={onChange}
                  name="status"
                  required
                />
              </div>
              <div className="file-input">
                <span className="details">Profile Photo</span>
                <input
                  type="file"
                  onChange={(event) => setFile(event.target.files[0])}
                  ref={fileRef}
                  name="photo"
                  required
                />
              </div>
            </div>
            <div className="form_footer">
              <button
                onClick={() => toggleModal(false)}
                type="button"
                className="btn btn-danger"
              >
                Cancel
              </button>
              <button type="submit" className="btn">
                Save
              </button>
            </div>
          </form>
        </div>
      </dialog>
      <ToastContainer />
    </>
  );
}

export default App;
