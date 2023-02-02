import { useState } from "react";

import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import LockOpenOutlinedIcon from "@mui/icons-material/LockOpenOutlined";
import Zoom from "@mui/material/Zoom";
import { CircularProgress, Tooltip } from "@mui/material";
import AddModeratorIcon from "@mui/icons-material/AddModerator";
import RemoveModeratorIcon from "@mui/icons-material/RemoveModerator";
import PasswordIcon from "@mui/icons-material/Password";

import Modal from "react-bootstrap/Modal";
import Table from "react-bootstrap/Table";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

import {
  addUser,
  editUserAttempts,
  editUserRole,
  resetUserPassword,
} from "../utils/apiService";

const HandleUsersModal = ({
  show,
  handleClose,
  users,
  setUsers,
  authenticated,
}) => {
  const [newUserName, setNewUserName] = useState("");
  const [newUserMessage, setNewUserMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const handleCloseOverride = () => {
    handleClose();
    setNewUserMessage("");
  };
  return (
    <Modal show={show === 4} onHide={handleCloseOverride}>
      <Modal.Header closeButton>
        <Modal.Title>Gestisci Utenti</Modal.Title>
      </Modal.Header>

      <Modal.Body className="handleUserModal-modal-body">
        {isLoading ? (
          <CircularProgress
            color="secondary"
            className="spinner"
            size={90}
            thickness={2.5}
          />
        ) : (
          <>
            <Table striped bordered hover size="sm" className="text-center">
              <thead>
                <tr className="align-middle">
                  <th>Utente</th>
                  <th>Stato</th>
                  <th>Ruolo</th>
                  <th>Resetta Password</th>
                </tr>
              </thead>
              <tbody className="align-middle">
                {users.map((user) => {
                  return (
                    <tr key={user.id}>
                      <td>{user.username}</td>
                      <td>
                        <div className="td-flex">
                          {user.attempts < 4 ? "Attivo" : "Bloccato"}
                          {user.attempts < 4 ? (
                            <Tooltip
                              title="Blocca Utente"
                              TransitionComponent={Zoom}
                            >
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
                        </div>
                      </td>
                      <td>
                        <div className="td-flex">
                          {user.role}
                          {user.role === "admin" ? (
                            <Tooltip
                              title="Rendi Utente"
                              TransitionComponent={Zoom}
                            >
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
                            <Tooltip
                              title="Rendi Admin"
                              TransitionComponent={Zoom}
                            >
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
                        </div>
                      </td>

                      <td>
                        <Tooltip
                          title="Resetta Password"
                          TransitionComponent={Zoom}
                        >
                          <PasswordIcon
                            role="button"
                            color={"primary"}
                            onClick={() =>
                              resetUserPassword(
                                user.id,
                                setIsLoading,
                                setUsers,
                                authenticated
                              )
                            }
                          />
                        </Tooltip>
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
                  addUser(
                    newUserName,
                    setIsLoading,
                    setUsers,
                    authenticated,
                    setNewUserMessage,
                    setNewUserName
                  )
                }
              >
                Aggiungi utente
              </Button>
            </Form.Group>
            {newUserMessage && <span className="error">{newUserMessage}</span>}
          </>
        )}
      </Modal.Body>

      <div className="delete-buttons">
        <Button
          variant="secondary"
          className="mb-2 mx-2"
          onClick={handleCloseOverride}
        >
          Chiudi
        </Button>
      </div>
    </Modal>
  );
};
export default HandleUsersModal;
