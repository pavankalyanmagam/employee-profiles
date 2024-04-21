import axios from "axios";

const API_URL = 'http://localhost:8080/employees';

export async function saveEmployee(employee) {

    return await axios.post(API_URL, employee);
}
export async function getEmployees(page = 0, size = 10) {

    return await axios.get(`${API_URL}?page=${page}&size=${size}`);
}
export async function getEmployee(id) {

    return await axios.get(`${API_URL}/${id}`);
}
export async function updatePhoto(formData) {

    return await axios.put(`${API_URL}/photo`, formData);
}
export async function updateEmployee(employee) {

    return await axios.post(API_URL, employee);
}
export async function deleteEmployee(id) {

    return await axios.delete(`${API_URL}/${id}`);
}
