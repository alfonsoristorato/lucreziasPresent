import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useEffect, useState } from "react";
import { showModal } from "./utils/reduxErrorModalSlice";
import Login from "./pages/Login";
import Entries from "./pages/Entries";
import "./styles/main.scss";
import ChangePassword from "./pages/ChangePassword";
import ErrorModal from "./components/ErrorModal";
import { useDispatch } from "react-redux";
import { actionImporter } from "./utils/apiService";

const App = () => {
  const [authenticated, setAuthenticated] = useState(false);
  const dispatch = useDispatch();

  const showErrorModal = (error) => {
    dispatch(showModal(error));
  };

  useEffect(() => {
    actionImporter.showErrorModal = (error) => showErrorModal(error);
  }, [showErrorModal]);

  return (
    <Router>
      <Routes>
        {!authenticated && (
          <Route
            path="/login"
            element={<Login setAuthenticated={setAuthenticated} />}
          ></Route>
        )}
        <Route
          path="/change-password"
          element={<ChangePassword authenticated={authenticated} />}
        ></Route>
        <Route
          path="/"
          element={<Entries authenticated={authenticated} />}
        ></Route>
      </Routes>
      <ErrorModal></ErrorModal>
    </Router>
  );
};
export default App;
