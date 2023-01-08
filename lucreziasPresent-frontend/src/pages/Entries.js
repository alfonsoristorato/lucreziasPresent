import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image";

import Fab from "@mui/material/Fab";
import AddIcon from "@mui/icons-material/Add";
import { IconButton, Tooltip } from "@mui/material";
import FilterListIcon from "@mui/icons-material/FilterList";
import DeleteIcon from "@mui/icons-material/Delete";
import ModeEditIcon from "@mui/icons-material/ModeEdit";
import useMediaQuery from "@mui/material/useMediaQuery";
import Zoom from "@mui/material/Zoom";

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
} from "react-icons/fa";
import moment from "moment";

import { deleteEntry, readEntries } from "../utils/apiService";
import AddEntryModal from "../components/AddEntryModal";
import DeleteEntryModal from "../components/DeleteEntryModal";

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
    setShow(false);
    setEditMode(false);
  };
  const handleCloseDelete = () => {
    setShowDelete(false);
    setDeleteMode(false);
  };
  const handleDelete = () => {
    deleteEntry(setEntries, deleteMode.id, authenticated, handleCloseDelete);
  };
  useEffect(() => {
    !authenticated && navigate("/login");
    authenticated && readEntries(setEntries, authenticated);
  }, [authenticated, navigate]);

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
              <h3 className="vertical-timeline-element-title">{entry.title}</h3>
              <div className="subtitle">
                <h4 className="vertical-timeline-element-subtitle">
                  da {entry.name}
                </h4>
                {authenticated.username === entry.owner && (
                  <div>
                    <Tooltip title="Modifica" TransitionComponent={Zoom}>
                      <IconButton
                        onClick={() => {
                          setEditMode(entry);
                          setShow(true);
                        }}
                      >
                        <ModeEditIcon sx={{ color: "white" }} />
                      </IconButton>
                    </Tooltip>
                    <Tooltip title="Cancella" TransitionComponent={Zoom}>
                      <IconButton
                        onClick={() => {
                          setDeleteMode(entry);
                          setShowDelete(true);
                        }}
                      >
                        <DeleteIcon sx={{ color: "white" }} />
                      </IconButton>
                    </Tooltip>
                  </div>
                )}
              </div>

              <p className="content">{entry.content}</p>
              {entry.file && (
                <Image
                  fluid
                  src={`data:image/jpeg;base64,${entry.file}`}
                  className="timeline-image"
                />
              )}
            </VerticalTimelineElement>
          );
        })}
        <div className={"floating-button-container"}>
          <Tooltip title="Filtra ricordi" TransitionComponent={Zoom}>
            <Fab
              onClick={() => setShow(true)}
              {...(useMediaQuery("(max-width:600px)") && { size: "small" })}
              {...(useMediaQuery("(min-width:1170px)") && {
                className: "out-of-boundary",
              })}
            >
              <FilterListIcon />
            </Fab>
          </Tooltip>
          <Tooltip title="Aggiungi ricordo" TransitionComponent={Zoom}>
            <Fab
              onClick={() => setShow(true)}
              {...(useMediaQuery("(max-width:600px)") && { size: "small" })}
              {...(useMediaQuery("(min-width:1170px)") && {
                className: "out-of-boundary",
              })}
            >
              <AddIcon />
            </Fab>
          </Tooltip>
        </div>
      </VerticalTimeline>
      <AddEntryModal
        show={show}
        handleClose={handleClose}
        editMode={editMode}
        setEntries={setEntries}
        authenticated={authenticated}
      ></AddEntryModal>

      <DeleteEntryModal
        showDelete={showDelete}
        handleCloseDelete={handleCloseDelete}
        deleteMode={deleteMode}
        handleDelete={handleDelete}
      ></DeleteEntryModal>
    </Container>
  );
};

export default Entries;
