import AddEntryForm from "./AddEntryForm";
import Modal from "react-bootstrap/Modal";

const AddEntryModal = ({
  show,
  handleClose,
  editMode,
  setEntries,
  authenticated,
  setIsLoading,
}) => {
  return (
    <Modal show={show === 1} onHide={handleClose}>
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
          setIsLoading={setIsLoading}
        />
      </Modal.Body>
    </Modal>
  );
};
export default AddEntryModal;
