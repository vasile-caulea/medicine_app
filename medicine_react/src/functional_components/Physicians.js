import {useEffect, useState} from "react";
import {GetPhysicians} from "../services/physicianServices";
import {useNavigate} from "react-router-dom";
import {PATHS, ROWS_PER_PAGE_OPTIONS} from "../services/utils";
import {TablePagination} from "@mui/material";


function SearchBar(props) {


    function searchCallback(e) {
        e.preventDefault()
        const form = document.getElementsByName('search-form')[0]
        props.updateFilters(
            {
                lastName: form['lastName'].value,
                specialization: form['specialization'].value
            }
        )
    }

    return (
        <form name="search-form" onSubmit={e => searchCallback(e)}>
            <input type="text" placeholder="Search by last name" name="lastName"/>
            <input type="text" placeholder="Search by specialization" name="specialization"/>
            <input type="submit" value={"Search"}/>
        </form>
    );
}

function Physician({physician}) {

    const navigator = useNavigate()

    return (
        <div className={'physician-card'}>
            <p>{physician.firstName} {physician.lastName}</p>
            <button onClick={() => {
                navigator(PATHS.PHYSICIAN, {state: {physician: physician}})
            }}>Make an appointment
            </button>
        </div>
    );
}

function Physicians() {

    const [physicians, setPhysicians] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [filters, setFilters] = useState({
        lastName: null,
        specialization: null,
        page: 0,
        itemsPerPage: 5
    })

    useEffect(() => {
        fetchData(filters.page, filters.itemsPerPage).then();
    }, [filters])

    function updateFilters(newFilters) {
        console.log(newFilters);
        setFilters({...filters, ...newFilters})
    }

    const fetchData = async () => {
        try {
            GetPhysicians(filters).then(data => {
                console.log(data)
                const physicianList = data['_embedded']['physicianList'];
                if (!physicianList) {
                    return
                }
                setPhysicians(physicianList.map((physician, index) =>
                    <Physician key={index} physician={physician}/>));
                setTotalItems(physicianList.length);
            }).catch(error => {
                alert(error)
            })
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const handleChangePage = (event, newPage) => {
        updateFilters({page: newPage})
    };

    const handleChangeRowsPerPage = (event) => {
        const newItemsPerPage = parseInt(event.target.value, 10);
        updateFilters({page: 0, itemsPerPage: newItemsPerPage})
    };

    return (
        <>
            <h1 className={'page-title'}>Physicians</h1>
            <div className={'physicians-component'}>
                <SearchBar updateFilters={updateFilters}/>
                <div className={'physicians-list'}>
                    {physicians}
                </div>
                <TablePagination
                    component="div"
                    page={filters.page}
                    onPageChange={handleChangePage}
                    rowsPerPage={filters.itemsPerPage}
                    rowsPerPageOptions={ROWS_PER_PAGE_OPTIONS}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                    count={totalItems}
                />
            </div>
        </>
    );
}

export default Physicians;