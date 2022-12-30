import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Entries from "./pages/Entries";

const App = () => {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/login" element={<Login />}></Route>

          <Route path="/" element={<Entries />}></Route>
        </Routes>
      </div>
    </Router>
  );
};
export default App;
