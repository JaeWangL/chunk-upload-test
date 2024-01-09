import { createAction } from '@reduxjs/toolkit';

export const uploadStart = createAction<{ file: File; sasUrl: string; }>('UPLOAD_START');
export const uploadSuccess = createAction<string>('UPLOAD_SUCCESS');
export const uploadFailure = createAction<string>('UPLOAD_FAILURE');
export const uploadProgress = createAction<number>('UPLOAD_PROGRESS');
