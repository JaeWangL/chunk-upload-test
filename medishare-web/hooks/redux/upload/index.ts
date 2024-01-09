import { uploadStart } from '@/redux/upload/actions';
import { useAppDispatch } from '../base';
import { useCallback } from 'react';

export function useStoreUpload() {
  const dispatch = useAppDispatch();

  const upload = useCallback((file: File, sasUrl: string) => {
    dispatch(uploadStart({ file, sasUrl}));
  }, []);

  return {
    upload,
  };
}
