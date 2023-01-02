import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Image from "react-bootstrap/Image";
import Modal from "react-bootstrap/Modal";
import Fab from "@mui/material/Fab";
import AddIcon from "@mui/icons-material/Add";

import { useEffect, useState } from "react";
import { deleteEntry, readEntries } from "../utils/apiService";
import {
  VerticalTimeline,
  VerticalTimelineElement,
} from "react-vertical-timeline-component";
import "react-vertical-timeline-component/style.min.css";
import {
  FaGrinHearts,
  FaBaby,
  FaBabyCarriage,
  FaHandHoldingHeart,
  FaHeart,
  FaHeartbeat,
  FaKissWinkHeart,
  FaSmileBeam,
  FaSmileWink,
  FaSmile,
  FaEdit,
  FaTrash,
} from "react-icons/fa";
import AddEntryForm from "../components/AddEntryForm";
import moment from "moment";
import { useNavigate } from "react-router-dom";

const Entries = ({ authenticated }) => {
  const [entries, setEntries] = useState([]);
  const [editMode, setEditMode] = useState(false);
  const [deleteMode, setDeleteMode] = useState(false);
  const [show, setShow] = useState(false);
  const [showDelete, setShowDelete] = useState(false);
  const navigate = useNavigate();

  const [icons] = useState([
    <FaSmile />,
    <FaSmileWink />,
    <FaSmileBeam />,
    <FaKissWinkHeart />,
    <FaHeartbeat />,
    <FaHeart />,
    <FaHandHoldingHeart />,
    <FaBabyCarriage />,
    <FaBaby />,
    <FaGrinHearts />,
  ]);
  const handleClose = () => {
    setEditMode(false);
    setShow(false);
  };
  const handleCloseDelete = () => {
    setDeleteMode(false);
    setShowDelete(false);
  };
  const handleDelete = () => {
    deleteEntry(setEntries, deleteMode.id, authenticated, handleCloseDelete);
  };
  useEffect(() => {
    !authenticated && navigate("/login");
    authenticated && readEntries(setEntries, authenticated);
  }, []);

  return (
    <Container className="entries">
      <div className="title">
        <h2>Ciao bimbo o bimba</h2>
        <h4>Ti abbiamo scritto dei ricordi</h4>
      </div>
      <VerticalTimeline>
        {entries.map((entry) => {
          return (
            <VerticalTimelineElement
              key={entry.id}
              className="vertical-timeline-element--work"
              contentStyle={{
                background: entry.color,
                color: "#fff",
              }}
              contentArrowStyle={{
                borderRight: `7px solid  ${entry.color}`,
              }}
              date={moment(entry.date).format("LL")}
              iconStyle={{ background: entry.color, color: "#fff" }}
              icon={icons[entry.icon]}
            >
              {authenticated.username === entry.owner && (
                <div className="action-icons">
                  <FaEdit
                    className="icon"
                    size={"1.2em"}
                    onClick={() => {
                      setEditMode(entry);
                      setShow(true);
                    }}
                  />

                  <FaTrash
                    className="icon"
                    size={"1.2em"}
                    onClick={() => {
                      setDeleteMode(entry);
                      setShowDelete(true);
                    }}
                  />
                </div>
              )}

              <h3 className="vertical-timeline-element-title">{entry.title}</h3>
              <h4 className="vertical-timeline-element-subtitle">
                da {entry.name}
              </h4>
              <p>{entry.content}</p>
              {entry.file && (
                <Image fluid src={`data:image/jpeg;base64,${entry.file}`} />
              )}
            </VerticalTimelineElement>
          );
        })}
        <Fab
          variant="extended"
          onClick={() => setShow(true)}
          className={"floating-button"}
        >
          <AddIcon sx={{ mr: 1 }} />
          Aggiungi ricordo
        </Fab>
      </VerticalTimeline>

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
            setEditMode={setEditMode}
            handleClose={handleClose}
            authenticated={authenticated}
          />
        </Modal.Body>
      </Modal>
      <Modal show={showDelete} onHide={handleCloseDelete}>
        <Modal.Header closeButton>
          <Modal.Title>Cancella ricordo</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Sei sicuro/a di voler cancellare {deleteMode.title}?
        </Modal.Body>

        <div className="delete-buttons">
          <Button
            variant="secondary"
            className="mb-2 mx-2"
            onClick={handleCloseDelete}
          >
            Chiudi
          </Button>
          <Button variant="danger" className="mb-2 mx-2" onClick={handleDelete}>
            Cancella
          </Button>
        </div>
      </Modal>
    </Container>
  );
};

export default Entries;
