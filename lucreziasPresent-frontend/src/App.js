import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useState } from "react";

import Login from "./pages/Login";
import Entries from "./pages/Entries";
import "./styles/main.scss";
import ChangePassword from "./pages/ChangePassword";

const App = () => {
  const [authenticated, setAuthenticated] = useState(false);

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
    </Router>
  );
};
export default App;
