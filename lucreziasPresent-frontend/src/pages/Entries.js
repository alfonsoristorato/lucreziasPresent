import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image";

import Fab from "@mui/material/Fab";
import AddIcon from "@mui/icons-material/Add";
import FilterListIcon from "@mui/icons-material/FilterList";
import DeleteIcon from "@mui/icons-material/Delete";
import ModeEditIcon from "@mui/icons-material/ModeEdit";
import Zoom from "@mui/material/Zoom";
import CircularProgress from "@mui/material/CircularProgress";
import IconButton from "@mui/material/IconButton";
import Tooltip from "@mui/material/Tooltip";
// import useMediaQuery from "@mui/material/useMediaQuery";

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
import FilterModal from "../components/FilterModal";
import { filterEntries } from "../utils/utils";

const Entries = ({ authenticated }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [entries, setEntries] = useState([]);
  const [filteredEntries, setFilteredEntries] = useState([]);
  const [authorsNames, setAuthorsNames] = useState([]);
  const [editMode, setEditMode] = useState(false);
  const [deleteMode, setDeleteMode] = useState(false);
  const [show, setShow] = useState(false);
  const [dateRange, setDateRange] = useState([null, null]);
  const [authorsSelected, setAuthorsSelected] = useState([]);
  const [startDate, endDate] = dateRange;
  const [width600Max, setWidth600Max] = useState(false);
  const [width1170Min, setWidth1170Min] = useState(false);
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
  const handleResize = () => {
    window.innerWidth < 600 ? setWidth600Max(true) : setWidth600Max(false);
    window.innerWidth > 1170 ? setWidth1170Min(true) : setWidth1170Min(false);
  };
  const handleClose = () => {
    setShow(false);
    setEditMode(false);
    setDeleteMode(false);
  };
  const handleDelete = () => {
    setIsLoading(true);
    deleteEntry(
      setEntries,
      deleteMode.id,
      authenticated,
      handleClose,
      setIsLoading
    );
  };
  useEffect(() => {
    if (!authenticated) {
      navigate("/login");
    } else {
      setIsLoading(true);
      handleResize();
      readEntries(setEntries, authenticated, setIsLoading);
    }
  }, [authenticated, navigate]);
  useEffect(() => {
    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);
  // for filtering
  useEffect(() => {
    setFilteredEntries(entries);
    let authors = new Set(entries.map((e) => e.name));
    let authorsArray = [];
    authors.forEach((author) => {
      authorsArray.push({ value: author, label: author });
    });
    setAuthorsNames(authorsArray);
  }, [entries]);

  useEffect(() => {
    filterEntries(entries, setFilteredEntries, dateRange, authorsSelected);
  }, [dateRange, authorsSelected, entries]);

  if (isLoading) {
    return (
      <CircularProgress
        color="secondary"
        className="spinner"
        size={90}
        thickness={2.5}
      />
    );
  } else {
    return (
      <Container className="entries">
        <div className="title">
          <h2>Ciao bimbo o bimba</h2>
          <h4>Ti abbiamo scritto dei ricordi</h4>
        </div>

        <VerticalTimeline>
          {filteredEntries.map((entry) => {
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
                <h3 className="vertical-timeline-element-title">
                  {entry.title}
                </h3>
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
                            setShow(1);
                          }}
                        >
                          <ModeEditIcon sx={{ color: "white" }} />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Cancella" TransitionComponent={Zoom}>
                        <IconButton
                          onClick={() => {
                            setDeleteMode(entry);
                            setShow(2);
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
                onClick={() => setShow(3)}
                {...(width600Max && { size: "small" })}
                {...(width1170Min && {
                  className: "out-of-boundary",
                })}
              >
                <FilterListIcon />
              </Fab>
            </Tooltip>
            <Tooltip title="Aggiungi ricordo" TransitionComponent={Zoom}>
              <Fab
                onClick={() => setShow(1)}
                {...(width600Max && { size: "small" })}
                {...(width1170Min && {
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
          setIsLoading={setIsLoading}
        ></AddEntryModal>

        <DeleteEntryModal
          show={show}
          handleClose={handleClose}
          deleteMode={deleteMode}
          handleDelete={handleDelete}
        ></DeleteEntryModal>

        <FilterModal
          show={show}
          handleClose={handleClose}
          startDate={startDate}
          authorsNames={authorsNames}
          endDate={endDate}
          setDateRange={setDateRange}
          authorsSelected={authorsSelected}
          setAuthorsSelected={setAuthorsSelected}
        ></FilterModal>
      </Container>
    );
  }
};

export default Entries;
