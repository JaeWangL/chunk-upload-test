import { createSlice } from '@reduxjs/toolkit';
import { ReduxStoreEnum } from '../constants';
import { UploadState } from './types';
import { uploadFailure, uploadProgress, uploadSuccess } from './actions';

const initialState: UploadState = {
  progress: 0,
  error: null,
  success: null,
};

const uploadSlice = createSlice({
  name: ReduxStoreEnum.UPLOAD,
  initialState,
  reducers: {
  },
  extraReducers: (builder) => {
    builder
    .addCase(uploadProgress, (state, action) => {
      state.progress = action.payload;
    })
    .addCase(uploadSuccess, (state, action) => {
      state.success = action.payload;
    })
    .addCase(uploadFailure, (state, action) => {
      state.error = action.payload;
    })
  },
});

export const uploadActions = uploadSlice.actions;
export const uploadName = uploadSlice.name;
export const uploadReducer = uploadSlice.reducer;
