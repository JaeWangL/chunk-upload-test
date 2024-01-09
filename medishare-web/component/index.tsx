import styles from '@/styles/Home.module.css'
import { ChangeEvent, useCallback, useEffect, useState } from 'react'
import { toast } from 'react-toastify';
import axios from 'axios';
import { throttle } from 'lodash';
import { chunkFileFunc, uploadChunksFunc } from '@/uploadUtils'

export function UploadComponent() {
  const uploadToastId = 'upload-progress-toast';
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const onFileChange = (event: ChangeEvent<HTMLInputElement>): void => {
    if (!event.target.files || event.target.files.length < 1) {
      return;
    }

    setSelectedFile(event.target.files[0]);
  };

  const handleUploadProgress = useCallback(
    throttle((progress: number) => {
      toast.update(uploadToastId, {
        render: `Upload Progress: ${progress}%`,
        position: "bottom-right",
        autoClose: false,
        closeOnClick: false,
        pauseOnHover: false,
        draggable: false,
        progress: progress / 100,
      });
    }, 200),
    []
  );

  useEffect(() => {
    return () => {
      console.log('Work');
      handleUploadProgress.cancel();
    };
  }, []);

  const chunkFile = useCallback((file: File, chunkSize = 1024 * 1024): Blob[] => { // 1MB
    return chunkFileFunc(file, chunkSize);
  }, []);

  const uploadChunks = useCallback(async (fileSize: number, chunks: Blob[], sasUrl: string, onProgress: (progress: number) => void) => {
    return uploadChunksFunc(fileSize, chunks, sasUrl, onProgress);
  }, []);

  const handleUplod = useCallback(async (): Promise<void> => {
    if (!selectedFile) {
      return;
    }

    toast('Starting upload...', {
      toastId: uploadToastId,
      position: "bottom-right",
      autoClose: false,
      closeOnClick: false,
      pauseOnHover: false,
      draggable: false,
      progress: 0,
    });

    const { data: sasUrl } = await axios.post<string>('http://localhost:8080/api/upload/sas/videos', {
      blobName: selectedFile.name,
      permission: 'w',
    });
    console.log(sasUrl);

    const chunks = chunkFile(selectedFile);
    await uploadChunks(selectedFile.size, chunks, sasUrl, handleUploadProgress);
  }, [selectedFile]);

    return (
        <>
        <button
        type="button"
        className={styles.card}
        onClick={handleUplod}
      >
        <h2>
          Upload <span>-&gt;</span>
        </h2>
        <p>
          Find in-depth information about Next.js features and&nbsp;API.
        </p>
      </button>
      <input type="file" onChange={onFileChange} accept="video/mp4" />
        </>
    )
}