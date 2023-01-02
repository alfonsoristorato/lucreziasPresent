import "./App.css";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  redirect,
  useNavigate,
} from "react-router-dom";
import Login from "./pages/Login";
import Entries from "./pages/Entries";
import "./styles/main.scss";
import { useState } from "react";
const App = () => {
  const [authenticated, setAuthenticated] = useState(false);

  return (
    <Router>
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
        <Route
          path="/"
          element={<Entries authenticated={authenticated} />}
        ></Route>
      </Routes>
    </Router>
  );
};
export default App;
