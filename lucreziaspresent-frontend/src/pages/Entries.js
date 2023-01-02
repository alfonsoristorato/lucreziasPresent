import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Button from "react-bootstrap/Button";
import Image from "react-bootstrap/Image";
import Modal from "react-bootstrap/Modal";

import Box from "@mui/material/Box";
import Fab from "@mui/material/Fab";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import FavoriteIcon from "@mui/icons-material/Favorite";
import NavigationIcon from "@mui/icons-material/Navigation";

import { useEffect, useRef, useState } from "react";
import { readEntries } from "../utils/apiService";
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
  const [show, setShow] = useState(false);
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
    setShow(false);
    setEditMode(false);
  };
  const handleShow = () => setShow(true);

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
                  onClick={() => setEditMode(true)}
                />
              </div>

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
          onClick={handleShow}
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
    </Container>
  );
};

export default Entries;
