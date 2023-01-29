import Modal from "react-bootstrap/Modal";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import LockOpenOutlinedIcon from "@mui/icons-material/LockOpenOutlined";
import Zoom from "@mui/material/Zoom";
import { Tooltip } from "@mui/material";
import AddModeratorIcon from "@mui/icons-material/AddModerator";
import RemoveModeratorIcon from "@mui/icons-material/RemoveModerator";
import Table from "react-bootstrap/Table";
import { editUserAttempts, editUserRole } from "../utils/apiService";

const HandleUsersModal = ({
  show,
  handleClose,
  users,
  setIsLoading,
  setUsers,
  authenticated,
}) => {
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
      </Modal.Body>
    </Modal>
  );
};
export default HandleUsersModal;
