import AddEntryForm from "./AddEntryForm";
import Modal from "react-bootstrap/Modal";

const AddEntryModal = ({
  show,
  handleClose,
  editMode,
  setEntries,
  authenticated,
}) => {
  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>
          {editMode ? "Modifica ricordo" : "Aggiungi ricordo"}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <AddEntryForm
          editMode={editMode}
          setEntries={setEntries}
          handleClose={handleClose}
          authenticated={authenticated}
        />
      </Modal.Body>
    </Modal>
  );
};
export default AddEntryModal;
