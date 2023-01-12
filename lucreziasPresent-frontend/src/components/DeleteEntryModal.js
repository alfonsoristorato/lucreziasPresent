import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
const DeleteEntryModal = ({ show, handleClose, deleteMode, handleDelete }) => {
  return (
    <Modal show={show === 2} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Cancella ricordo</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Sei sicuro/a di voler cancellare {deleteMode.title}?
      </Modal.Body>

      <div className="delete-buttons">
        <Button variant="secondary" className="mb-2 mx-2" onClick={handleClose}>
          Chiudi
        </Button>
        <Button variant="danger" className="mb-2 mx-2" onClick={handleDelete}>
          Cancella
        </Button>
      </div>
    </Modal>
  );
};
export default DeleteEntryModal;
