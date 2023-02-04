import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import Select from "react-select";
import FormLabel from "react-bootstrap/esm/FormLabel";
const FilterModal = ({
  show,
  handleClose,
  startDate,
  authorsNames,
  endDate,
  setDateRange,
  authorsSelected,
  setAuthorsSelected,
}) => {
  return (
    <Modal show={show === 3} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Filtra ricordi</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="mb-3">
          <FormLabel>Date</FormLabel>
          <DatePicker
            selectsRange={true}
            startDate={startDate}
            endDate={endDate}
            onChange={(dates) => {
              setDateRange(dates);
            }}
            className="date-picker"
            isClearable={true}
            placeholderText="  Seleziona un intervallo di date"
          />
        </div>
        <FormLabel>Autori</FormLabel>
        <div>
          <Select
            options={authorsNames}
            isMulti
            name="authors"
            className="basic-multi-select"
            classNamePrefix="select"
            placeholder="Seleziona uno o più autori"
            onChange={(selection) => setAuthorsSelected(selection)}
            defaultValue={authorsSelected}
          />
        </div>
      </Modal.Body>

      <div className="buttons-flex">
        <Button variant="secondary" className="mb-2 mx-2" onClick={handleClose}>
          Chiudi
        </Button>
      </div>
    </Modal>
  );
};
export default FilterModal;
