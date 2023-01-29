import Modal from "react-bootstrap/Modal";
import { useState } from "react";
import { useEffect } from "react";
import { getUsers } from "../utils/apiService";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import LockOpenOutlinedIcon from "@mui/icons-material/LockOpenOutlined";
import Zoom from "@mui/material/Zoom";
import { Tooltip } from "@mui/material";

import Table from "react-bootstrap/Table";

const HandleUsersModal = ({
  show,
  handleClose,
  authenticated,
  setIsLoading,
}) => {
  const [users, setUsers] = useState([]);
  useEffect(() => {
    if (show === 4) {
      setIsLoading(true);
      getUsers(setUsers, setIsLoading, authenticated);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  return (
    <Modal show={show === 4} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Gestisci Utenti</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Table striped bordered hover size="sm" className="text-center">
          <thead>
            <tr>
              <th>Utente</th>
              <th>Stato</th>
              <th>Ruolo</th>
              <th>Blocca / Sblocca</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => {
              return (
                <tr>
                  <td>{user.username}</td>
                  <td>{user.attempts < 4 ? "Attivo" : "Bloccato"}</td>
                  <td>{user.role}</td>
                  <td>
                    {user.attempts < 4 ? (
                      <Tooltip title="Blocca Utente" TransitionComponent={Zoom}>
                        <LockOutlinedIcon color={"error"} />
                      </Tooltip>
                    ) : (
                      <Tooltip
                        title="Sblocca Utente"
                        TransitionComponent={Zoom}
                      >
                        <LockOpenOutlinedIcon color={"success"} />
                      </Tooltip>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </Modal.Body>
    </Modal>
  );
};
export default HandleUsersModal;
