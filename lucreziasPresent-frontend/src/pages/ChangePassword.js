import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import { useLocation } from "react-router-dom";
import { useForm } from "react-hook-form";

import { changePassword } from "../utils/apiService";

const ChangePassword = ({ authenticated }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [referredFromLogin] = useState(
    location.state?.previousPage === "/login" ? true : false
  );

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const onSubmit = (data) => {
    changePassword(authenticated, data, setPasswordChangeRequested);
  };
  const [passwordChangeRequested, setPasswordChangeRequested] = useState(false);
  const handleClose = (passwordChangeRequested) => {
    if (passwordChangeRequested === "Password Cambiata.") {
      navigate(0);
    } else {
      setPasswordChangeRequested(false);
    }
  };
  useEffect(() => {
    if (!authenticated) {
      navigate("/login");
    }
  }, [authenticated, navigate]);

  return (
    <Container>
      <Row>
        <Col xs={12} className={"m-2 border-container"}>
          <Modal show={!passwordChangeRequested}>
            <Modal.Header>
              <Modal.Title>Cambia Password</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Form onSubmit={handleSubmit(onSubmit)}>
                <Form.Group
                  className="mb-3"
                  controlId="formBasicChangeUsername"
                >
                  <Form.Label>Vecchia Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Vecchia Password"
                    autoComplete="new-password"
                    {...register("password", { required: true })}
                  />
                  {errors.password && (
                    <span className="error">Questo campo è obbligatorio.</span>
                  )}
                </Form.Group>

                <Form.Group
                  className="mb-3"
                  controlId="formBasicChangePassword"
                >
                  <Form.Label>
                    Nuova Password
                    <p className="new-password-subtitle">
                      Deve avere almeno una lettera grande e due caratteri
                      speciali diversi.
                    </p>
                  </Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Nuova Password"
                    autoComplete="new-password"
                    {...register("newPassword", { required: true })}
                  />
                  {errors.password && (
                    <span className="error">Questo campo è obbligatorio.</span>
                  )}
                </Form.Group>
                <Form.Group
                  className="mb-3"
                  controlId="formBasicChangePasswordRepeated"
                >
                  <Form.Label>Ripeti Nuova Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Nuova Password"
                    autoComplete="new-password"
                    {...register("newPasswordRepeated", { required: true })}
                  />
                  {errors.password && (
                    <span className="error">Questo campo è obbligatorio.</span>
                  )}
                </Form.Group>
                <Row>
                  {!referredFromLogin && (
                    <Button
                      variant="secondary"
                      className="mb-2"
                      onClick={() => navigate(-1)}
                    >
                      Vai Indietro
                    </Button>
                  )}
                  <Button variant="primary" type="submit">
                    Salva
                  </Button>
                </Row>
              </Form>
            </Modal.Body>
          </Modal>
          <Modal
            show={passwordChangeRequested}
            onHide={() => handleClose(passwordChangeRequested)}
          >
            <Modal.Header closeButton></Modal.Header>
            <Modal.Body>{passwordChangeRequested}</Modal.Body>
          </Modal>
        </Col>
      </Row>
    </Container>
  );
};
export default ChangePassword;
