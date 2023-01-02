import axios from "axios";

const apiOrigin = "http://localhost:8080";

export const callApi = async (route, methodUsed, bodyUsed, token) => {
  try {
    const axiosInit = {
      headers: {
        Authorization: "Basic " + window.btoa("alfo" + ":" + "alfo"),
      },
      ...(bodyUsed && { data: bodyUsed }),
      ...(methodUsed && { method: methodUsed }),
    };

    const response = await axios.request(`${apiOrigin}/${route}`, axiosInit);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const readEntries = async (setter) => {
  try {
    let token;
    // tokenPassed ? (token = tokenPassed) : (token = await tokenGenerator());

    let response = await callApi("entry", "GET", null, null);
    setter(response);
  } catch (error) {
    throw error;
  }
};

export const addEntry = async (setter, data) => {
  try {
    await callApi("entry", "POST", data);
    await readEntries(setter);
  } catch (error) {
    throw error;
  }
};

// export const editEntry = async (
//   tokenGenerator,
//   setter,
//   data,
//   id,
//   email,
//   setEditMode
// ) => {
//   try {
//     let token = await tokenGenerator("edit:entries");

//     await callApi(`journalEntries/${id}`, "PUT", data, token);
//     await readEntries(null, email, setter, token);
//     setEditMode(false);
//   } catch (error) {
//     throw error;
//   }
// };

// export const deleteEntry = async (tokenGenerator, setter, data) => {
//   try {
//     let token = await tokenGenerator("delete:entries");
//     await callApi(`journalEntries/${data.id}`, "DELETE", null, token);
//     await readEntries(null, data.email, setter, token);
//   } catch (error) {
//     throw error;
//   }
// };
