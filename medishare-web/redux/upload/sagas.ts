import { call, put, takeLatest } from 'redux-saga/effects';
import { PayloadAction } from '@reduxjs/toolkit';
import { uploadStart, uploadSuccess, uploadFailure, uploadProgress } from './actions';
import { chunkFileFunc, uploadChunksFunc } from '../../uploadUtils';

function* handleUpload(action: PayloadAction<{ file: File; sasUrl: string; }>) {
  try {
    const payload = action.payload;
    const chunks = chunkFileFunc(payload.file);
    const onProgress = (progress: number) => {
      put(uploadProgress(progress));
    };

    yield call(uploadChunksFunc, payload.file.size, chunks, payload.sasUrl, onProgress);

    yield put(uploadSuccess('succeed'));
  } catch (error) {
    if (error instanceof Error) {
      yield put(uploadFailure(error.message));
    }
  }
}

export function* watchUploadSaga() {
  yield takeLatest(uploadStart.type, handleUpload);
}
