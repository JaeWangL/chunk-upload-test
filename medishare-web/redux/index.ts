import { combineReducers, configureStore } from '@reduxjs/toolkit';
import { uploadName, uploadReducer } from './upload/slice';
import createSagaMiddleware from 'redux-saga';
import { watchUploadSaga } from './upload/sagas';
import { all } from 'redux-saga/effects';

const sagaMiddleware = createSagaMiddleware();
function* rootSaga() {
  yield all([watchUploadSaga()]);
}

const rootReducer = combineReducers({
  [uploadName]: uploadReducer,
});

export const store = configureStore({
  reducer: rootReducer,
  devTools: false,
  // @ts-ignore
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(sagaMiddleware),
});

sagaMiddleware.run(rootSaga);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
