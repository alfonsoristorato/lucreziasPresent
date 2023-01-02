import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Entries from "./pages/Entries";
import "./styles/main.scss";
import { useState } from "react";
const App = () => {
  const [authenticated, setAuthenticated] = useState(false);
  return (
    <Router>
      <div>
        <Routes>
          {!authenticated && (
            <Route
              path="/login"
              element={
                <Login
                  authenticated={authenticated}
                  setAuthenticated={setAuthenticated}
                />
              }
            ></Route>
          )}

          <Route path="/" element={<Entries />}></Route>
        </Routes>
      </div>
    </Router>
  );
};
export default App;
