import Modal from "react-bootstrap/Modal";
import { useSelector, useDispatch } from "react-redux";
import { hideModal } from "../utils/reduxErrorModalSlice";
import { useEffect } from "react";

const ErrorModal = () => {
  const show = useSelector((state) => state.errorModal.value);
  const error = useSelector((state) => state.errorModal.error);

  const dispatch = useDispatch();

  return (
    <Modal show={show} onHide={() => dispatch(hideModal())}>
      <Modal.Header closeButton>
        <Modal.Title>Si è verificato un errore.</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div>Errore: {error}</div>
        <div>Assicurati di riportarlo al gestore dell'app.</div>
      </Modal.Body>
    </Modal>
  );
};
export default ErrorModal;
