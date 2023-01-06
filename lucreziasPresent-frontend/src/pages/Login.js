import { useState } from "react";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import { useForm } from "react-hook-form";
import { login } from "../utils/apiService";
import { useNavigate } from "react-router-dom";
const Login = ({ setAuthenticated }) => {
  const navigate = useNavigate();
  const [authError, setAuthError] = useState(false);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const onSubmit = (data) => {
    login(setAuthenticated, setAuthError, data, navigate);
  };

  return (
    <Container>
      <Row>
        <Col xs={12} className={"m-2 border-container"}>
          <Modal show={true}>
            <Modal.Header>
              <Modal.Title>Login</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Form onSubmit={handleSubmit(onSubmit)}>
                <Form.Group className="mb-3" controlId="formBasicUsername">
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Username"
                    {...register("username", { required: true })}
                  />
                  {errors.username && (
                    <span className="error">Questo campo è obbligatorio.</span>
                  )}
                </Form.Group>

                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Password"
                    {...register("password", { required: true })}
                  />
                  {errors.password && (
                    <span className="error">Questo campo è obbligatorio.</span>
                  )}
                </Form.Group>
                <Row>
                  <Button variant="primary" type="submit">
                    Login
                  </Button>
                  {authError && (
                    <span className="error">{authError}</span>
                  )}
                </Row>
              </Form>
            </Modal.Body>
          </Modal>
        </Col>
      </Row>
    </Container>
  );
};
export default Login;
