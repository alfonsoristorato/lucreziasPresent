import { useState } from "react";

import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import { useForm } from "react-hook-form";

import { addEntry, editEntry } from "../utils/apiService";

const AddEntryForm = ({
  setEntries,
  editMode,
  handleClose,
  authenticated,
  setIsLoading,
}) => {
  const [fileTypeError, setFileTypeError] = useState(false);
  const [fileSizeError, setFileSizeError] = useState(false);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const onSubmit = (data) => {
    setFileTypeError(false);
    setFileSizeError(false);
    let validForm = true;
    if (!editMode && data.file[0]) {
      if (
        !["image/jpeg", "image/png", "image/jpg"].includes(data.file[0]?.type)
      ) {
        setFileTypeError(true);
        validForm = false;
      }
      if (data.file[0].size > 10485760) {
        setFileSizeError(true);
        validForm = false;
      }
    }
    if (validForm) {
      setIsLoading(true);
      let formData = new FormData();
      !editMode && data.file[0] && formData.append("file", data.file[0]);
      formData.append("color", data.color);
      formData.append("content", data.content);
      formData.append("date", data.date);
      !editMode && formData.append("icon", Math.floor(Math.random() * 10));
      formData.append("name", data.name);
      formData.append("title", data.title);

      editMode
        ? editEntry(
            setEntries,
            formData,
            editMode.id,
            authenticated,
            handleClose,
            setIsLoading
          )
        : addEntry(
            setEntries,
            formData,
            authenticated,
            handleClose,
            setIsLoading
          );
    }
  };
  return (
    <Form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">
      <Form.Group className="mb-3">
        <Form.Label>Autore</Form.Label>
        <Form.Control
          placeholder="Mamma, Papà, Nonna, Nonno ecc..."
          defaultValue={editMode ? editMode.name : ""}
          {...register("name", { required: true })}
        />
        {errors.name && (
          <span className="error">Questo campo è obbligatorio.</span>
        )}
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Titolo</Form.Label>
        <Form.Control
          placeholder="Titolo del ricordo"
          defaultValue={editMode ? editMode.title : ""}
          {...register("title", { required: true })}
        ></Form.Control>
        {errors.title && (
          <span className="error">Questo campo è obbligatorio.</span>
        )}
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Ricordo</Form.Label>
        <Form.Control
          as="textarea"
          rows={6}
          placeholder="Scrivi il tuo ricordo..."
          defaultValue={editMode ? editMode.content : ""}
          {...register("content", { required: true })}
        />
        {errors.content && (
          <span className="error">Questo campo è obbligatorio.</span>
        )}
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Data</Form.Label>
        <Form.Control
          as="input"
          type="date"
          min="2000-01-01"
          defaultValue={editMode ? editMode.date : ""}
          {...register("date", { required: true })}
        />
        {errors.date && (
          <span className="error">Questo campo è obbligatorio.</span>
        )}
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Colore</Form.Label>
        <Form.Control
          as="input"
          type="color"
          defaultValue={editMode ? editMode.color : "#019ddf"}
          {...register("color", { required: true })}
        />
        {errors.color && (
          <span className="error">Questo campo è obbligatorio.</span>
        )}
      </Form.Group>
      {!editMode && (
        <Form.Group className="mb-3">
          <Form.Label>Vuoi aggiungere una foto?</Form.Label>
          <Form.Control
            as="input"
            type="file"
            accept="image/*"
            onChangeCapture={() => {
              setFileTypeError(false);
              setFileSizeError(false);
            }}
            {...register("file")}
          />
          {fileTypeError && (
            <span className="error">
              Puoi aggiungere solo i seguenti formati: JPEG, JPG, PNG.
            </span>
          )}
          {fileSizeError && (
            <span className="error">Il file non può superare i 10 MB.</span>
          )}
        </Form.Group>
      )}

      <Row>
        <Button variant="secondary" className="mb-2" onClick={handleClose}>
          Chiudi
        </Button>
        <Button variant="primary" type="submit" className="mb-2">
          Salva
        </Button>
      </Row>
    </Form>
  );
};
export default AddEntryForm;
