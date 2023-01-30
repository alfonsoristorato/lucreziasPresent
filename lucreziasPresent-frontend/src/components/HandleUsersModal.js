import Modal from "react-bootstrap/Modal";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import LockOpenOutlinedIcon from "@mui/icons-material/LockOpenOutlined";
import Zoom from "@mui/material/Zoom";
import { Tooltip } from "@mui/material";
import AddModeratorIcon from "@mui/icons-material/AddModerator";
import RemoveModeratorIcon from "@mui/icons-material/RemoveModerator";
import Table from "react-bootstrap/Table";
import { addUser, editUserAttempts, editUserRole } from "../utils/apiService";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { useState } from "react";

const HandleUsersModal = ({
  show,
  handleClose,
  users,
  setIsLoading,
  setUsers,
  authenticated,
}) => {
  const [newUserName, setNewUserName] = useState("");
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
              <th>Cambia Ruolo</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => {
              return (
                <tr key={user.id}>
                  <td>{user.username}</td>
                  <td>{user.attempts < 4 ? "Attivo" : "Bloccato"}</td>
                  <td>{user.role}</td>
                  <td>
                    {user.attempts < 4 ? (
                      <Tooltip title="Blocca Utente" TransitionComponent={Zoom}>
                        <LockOutlinedIcon
                          role="button"
                          color={"error"}
                          onClick={() =>
                            editUserAttempts(
                              user.id,
                              user.attempts,
                              setIsLoading,
                              setUsers,
                              authenticated
                            )
                          }
                        />
                      </Tooltip>
                    ) : (
                      <Tooltip
                        title="Sblocca Utente"
                        TransitionComponent={Zoom}
                      >
                        <LockOpenOutlinedIcon
                          role="button"
                          color={"success"}
                          onClick={() =>
                            editUserAttempts(
                              user.id,
                              user.attempts,
                              setIsLoading,
                              setUsers,
                              authenticated
                            )
                          }
                        />
                      </Tooltip>
                    )}
                  </td>
                  <td>
                    {user.role === "admin" ? (
                      <Tooltip title="Rendi Utente" TransitionComponent={Zoom}>
                        <RemoveModeratorIcon
                          role="button"
                          color={"error"}
                          onClick={() =>
                            editUserRole(
                              user.id,
                              user.role,
                              setIsLoading,
                              setUsers,
                              authenticated
                            )
                          }
                        />
                      </Tooltip>
                    ) : (
                      <Tooltip title="Rendi Admin" TransitionComponent={Zoom}>
                        <AddModeratorIcon
                          role="button"
                          color={"success"}
                          onClick={() =>
                            editUserRole(
                              user.id,
                              user.role,
                              setIsLoading,
                              setUsers,
                              authenticated
                            )
                          }
                        />
                      </Tooltip>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </Table>
        <Form.Group className="mb-3 add-user">
          <Form.Control
            placeholder="Nome Utente"
            onChange={(e) => setNewUserName(e.target.value)}
          />
          <Button
            style={{ width: "100%" }}
            {...((users.some((user) => user.username === newUserName) ||
              newUserName === authenticated.username ||
              !newUserName) && {
              disabled: true,
            })}
            onClick={() =>
              addUser(newUserName, setIsLoading, setUsers, authenticated)
            }
          >
            Aggiungi utente
          </Button>
        </Form.Group>
      </Modal.Body>
      <div className="delete-buttons">
        <Button variant="secondary" className="mb-2 mx-2" onClick={handleClose}>
          Chiudi
        </Button>
      </div>
    </Modal>
  );
};
export default HandleUsersModal;
