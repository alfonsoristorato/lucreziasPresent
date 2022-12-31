import { useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import { addEntry, editEntry } from "../utils/apiService";
import moment from "moment";
const AddEntryForm = ({ setEntries, editMode, setEditMode, handleClose }) => {
  //   const { user } = useAuth0();
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();
  const onSubmit = (data) => {
    let formData = new FormData();
    formData.append("file", data.file[0]);
    formData.append("color", data.color);
    formData.append("content", data.content);
    formData.append("date", data.date);
    formData.append("icon", 1);
    formData.append("name", "Alfo");
    formData.append("title", data.title);

    // !editMode && (data.email = user.email);

    // editMode
    //   ?
    //     editEntry(
    //           tokenGenerator,
    //           setEntries,
    //           data,
    //           editMode.id,
    //           editMode.emailHashed,
    //           setEditMode
    //         )
    //   : addEntry(setEntries, data);
    addEntry(setEntries, formData);
    console.log(data);
    // handleClose();
  };
  return (
    <Form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">
      <Form.Group className="mb-3">
        <Form.Label>Titolo</Form.Label>
        <Form.Control
          placeholder="Titolo del ricordo"
          defaultValue={editMode ? editMode.title : ""}
          {...register("title", { required: true })}
        ></Form.Control>
        {errors.title && <span>Questo campo è obbligatorio</span>}
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
        {errors.content && <span>Questo campo è obbligatorio</span>}
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
        {errors.date && <span>Questo campo è obbligatorio</span>}
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Colore</Form.Label>
        <Form.Control
          as="input"
          type="color"
          defaultValue={editMode ? editMode.color : "#019ddf"}
          {...register("color", { required: true })}
        />
        {errors.color && <span>Questo campo è obbligatorio</span>}
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Vuoi aggiungere una foto o un video?</Form.Label>
        <Form.Control
          as="input"
          type="file"
          defaultValue={editMode ? editMode.data : ""}
          {...register("file")}
        />
      </Form.Group>
      <Button variant="secondary" className="mb-2" onClick={handleClose}>
        Chiudi
      </Button>
      <Button variant="primary" type="submit" className="mb-2">
        Salva
      </Button>
      {editMode && (
        <Button
          variant="secondary"
          onClick={() => {
            setEditMode(false);
          }}
          className="mb-2"
        >
          Cancel Edit
        </Button>
      )}
    </Form>
  );
};
export default AddEntryForm;
