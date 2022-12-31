import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { useEffect, useRef, useState } from "react";
import { readEntries } from "../utils/apiService";
import {
  VerticalTimeline,
  VerticalTimelineElement,
} from "react-vertical-timeline-component";
import "react-vertical-timeline-component/style.min.css";
import { FaGrinHearts } from "react-icons/fa";
import AddEntryForm from "../components/AddEntryForm";

const Entries = () => {
  const [entries, setEntries] = useState([]);
  const [editMode, setEditMode] = useState(false);
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  useEffect(() => {
    readEntries(setEntries);
  }, []);
  return (
    <>
      {entries.length > 0 ? (
        <VerticalTimeline>
          {console.log(entries)}
          {entries.map((entry) => {
            return (
              <VerticalTimelineElement
                key={entry.id}
                className="vertical-timeline-element--work"
                contentStyle={{
                  background: "rgb(33, 150, 243)",
                  color: "#fff",
                }}
                contentArrowStyle={{
                  borderRight: "7px solid  rgb(33, 150, 243)",
                }}
                date={entry.date}
                iconStyle={{ background: entry.color, color: "#fff" }}
                icon={<FaGrinHearts />}
              >
                <h3 className="vertical-timeline-element-title">
                  {entry.title}
                </h3>
                <h4 className="vertical-timeline-element-subtitle">
                  da {entry.name}
                </h4>
                <p>{entry.content}</p>
              </VerticalTimelineElement>
            );
          })}
        </VerticalTimeline>
      ) : (
        <div>Loading</div>
      )}

      <Button variant="primary" onClick={handleShow}>
        Launch demo modal
      </Button>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>
            {editMode ? "Modifica ricordo" : "Aggiungi ricordo"}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <AddEntryForm
            editMode={editMode}
            setEditMode={setEditMode}
            handleClose={handleClose}
          />
        </Modal.Body>
        {/* <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleClose}>
            Save Changes
          </Button>
        </Modal.Footer> */}
      </Modal>
    </>
  );
};

export default Entries;
