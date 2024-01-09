import axios from 'axios';

export function chunkFileFunc(file: File, chunkSize = 1024 * 1024): Blob[] { // 1MB
  const chunks = [];
  let currentByte = 0;
  
  while (currentByte < file.size) {
    const chunk = file.slice(currentByte, currentByte + chunkSize);
    chunks.push(chunk);
    currentByte += chunkSize;
  }
  
  return chunks;
}

export async function uploadChunksFunc(fileSize: number, chunks: Blob[], sasUrl: string, onProgress: (progress: number) => void) {
  let uploadPromises: Promise<string>[] = [];
  let uploadedBytes = 0;
  
  chunks.forEach((chunk, i) => {
    const blockId = window.btoa(String(i).padStart(5, "0"));
    const blockUrl = `${sasUrl}&comp=block&blockid=${blockId}`;
  
    uploadPromises.push(
      axios.put(blockUrl, chunk, {
        headers: {
          'x-ms-blob-type': 'BlockBlob',
        },
        onUploadProgress: (progressEvent) => {
          let currentChunkSize = Math.min(fileSize - uploadedBytes, chunk.size);
          uploadedBytes += progressEvent.loaded - (progressEvent.total! - currentChunkSize);
          const totalProgress = Math.round((uploadedBytes / fileSize) * 100);
          onProgress(totalProgress);
        },
      }).then(response => {
        if (response.status !== 201) {
          console.error('Upload block failed:', response);
          throw new Error('Chunk upload failed');
        }
        return blockId;
      })
    );
  });
  
  const blockIds = await Promise.all(uploadPromises);
  const xml = `<BlockList>${blockIds.map(id => `<Latest>${id}</Latest>`).join("")}</BlockList>`;
  const commitUrl = `${sasUrl}&comp=blocklist`;
  const putBlockListResponse = await axios.put(commitUrl, xml, {
    headers: {
      'Content-Type': 'application/xml',
    },
  });
  
  if (putBlockListResponse.status !== 201 && putBlockListResponse.status !== 200) {
    console.error('Commit block list failed:', putBlockListResponse);
    throw new Error('Commit block list failed');
  }
}
