import React, { useState, useEffect, useRef } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getEmployee } from '../api/EmployeeService';
import { toastError, toastSuccess } from '../api/ToastService';

const EmployeeDetails = ({ updateEmployee, updateImage }) => {
    const inputRef = useRef();
    const [employee, setEmployee] = useState({
        id: '',
        name: '',
        email: '',
        phone: '',
        address: '',
        title: '',
        status: '',
        photoUrl: ''
    });

    const { id } = useParams();

    const fetchEmployee = async (id) => {
        try {
            const { data } = await getEmployee(id);
            setEmployee(data);
            console.log(data);
            //toastSuccess('Contact retrieved');
        } catch (error) {
            console.log(error);
            toastError(error.message);
        }
    };

    const selectImage = () => {
        inputRef.current.click();
    };

    const udpatePhoto = async (file) => {
        try {
            const formData = new FormData();
            formData.append('file', file, file.name);
            formData.append('id', id);
            await updateImage(formData);
            setEmployee((prev) => ({ ...prev, photoUrl: `${prev.photoUrl}?updated_at=${new Date().getTime()}` }));
            toastSuccess('Photo Updated!')
        } catch (error) {
            console.log(error);
            toastError(error.message);
        }
    };

    const onChange = (event) => {
        setEmployee({ ...employee, [event.target.name]: event.target.value });
    };

    const onUpdateEmployee = async (event) => {
        event.preventDefault();
        await updateEmployee(employee);        
        fetchEmployee(id);
        toastSuccess('Employee Updated!')
    };

    useEffect(() => {
        fetchEmployee(id);
    }, []);

    return (
        <>
            <Link to={'/employees'} className='link'><i className='bi bi-arrow-left'></i> Back to list</Link>
            <div className='profile'>
                <div className='profile__details'>
                    <img src={employee.photoUrl} alt={`Profile photo of ${employee.name}`} />
                    <div className='profile__metadata'>
                        <p className='profile__name'>{employee.name}</p>
                        <p className='profile__muted'>JPG, GIF, or PNG. Max size of 10MG</p>
                        <button onClick={selectImage} className='btn'><i className='bi bi-cloud-upload'></i> Change Photo</button>
                    </div>
                </div>
                <div className='profile__settings'>
                    <div>
                        <form onSubmit={onUpdateEmployee} className="form">
                            <div className="user-details">
                                <input type="hidden" defaultValue={employee.id} name="id" required />
                                <div className="input-box">
                                    <span className="details">Name</span>
                                    <input type="text" value={employee.name} onChange={onChange} name="name" required />
                                </div>
                                <div className="input-box">
                                    <span className="details">Email</span>
                                    <input type="text" value={employee.email} onChange={onChange} name="email" required />
                                </div>
                                <div className="input-box">
                                    <span className="details">Phone</span>
                                    <input type="text" value={employee.phone} onChange={onChange} name="phone" required />
                                </div>
                                <div className="input-box">
                                    <span className="details">Address</span>
                                    <input type="text" value={employee.address} onChange={onChange} name="address" required />
                                </div>
                                <div className="input-box">
                                    <span className="details">Title</span>
                                    <input type="text" value={employee.title} onChange={onChange} name="title" required />
                                </div>
                                <div className="input-box">
                                    <span className="details">Status</span>
                                    <input type="text" value={employee.status} onChange={onChange} name="status" required />
                                </div>
                            </div>
                            <div className="form_footer">
                                <button type="submit" className="btn">Save</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <form style={{ display: 'none' }}>
                <input type='file' ref={inputRef} onChange={(event) => udpatePhoto(event.target.files[0])} name='file' accept='image/*' />
            </form>
        </>
    )
}

export default EmployeeDetails;