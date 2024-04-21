import React from 'react';
import Employee from './Employee';

const EmployeeList = ({ data, currentPage, getAllEmployees }) => {
    return (
        <main className='main'>
            {data?.content?.length === 0 && <div>No Employees. Please add a new employee</div>}

            <ul className='employee__list'>
                {data?.content?.length > 0 && data.content.map(employee => <Employee employee={employee} key={employee.id} />)}
            </ul>

            {data?.content?.length > 0 && data?.totalPages > 1 &&
            <div className='pagination'>
                <a onClick={() => getAllEmployees(currentPage - 1)} className={0 === currentPage ? 'disabled' : ''}>&laquo;</a>

                { data && [...Array(data.totalPages).keys()].map((page, index) => 
                    <a onClick={() => getAllEmployees(page)} className={currentPage === page ? 'active' : ''} key={page}>{page + 1}</a>)}


                <a onClick={() => getAllEmployees(currentPage + 1)} className={data.totalPages === currentPage + 1 ? 'disabled' : ''}>&raquo;</a>
            </div>            
            }

        </main>
    )
}

export default EmployeeList;