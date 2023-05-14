import { createSlice } from "@reduxjs/toolkit";

export const reduxErrorModalSlice = createSlice({
  name: "errorModal",
  initialState: {
    value: false,
    error: "",
  },
  reducers: {
    showModal: (state, error) => {
      state.value = true;
      state.error = error.payload;
    },
    hideModal: (state) => {
      state.value = false;
      state.error = "";
    },
  },
});

export const { showModal, hideModal } = reduxErrorModalSlice.actions;

export default reduxErrorModalSlice.reducer;
