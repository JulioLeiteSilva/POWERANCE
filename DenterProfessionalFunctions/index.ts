import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const firebase = admin.initializeApp();

type User = {
  name: string,
  email: string,
  phone: string,
  cep: string,
  adress1: string,
  adress2: string,
  adress3: string,
  miniResume: string,
  status: boolean | false,
  fcmToken: string | undefined,
  uid: string,
}

type CustomResponse = {
  status: string | unknown,
  message: string | unknown,
  payload: unknown,
}

function hasAccountData(data: User) {
  if (data.name != undefined &&
      data.email != undefined &&
      data.phone != undefined &&
      data.cep != undefined &&
      data.adress1 != undefined &&
      data.adress2 != undefined &&
      data.adress3 != undefined &&
      data.miniResume != undefined &&
      data.uid != undefined &&
      data.fcmToken != undefined) {
    return true;
  } else {
    return false;
  }
}

export const setUserProfile = functions
  .region("southamerica-east1")
  .runWith({enforceAppCheck: false})
  .https
  .onCall(async (data, context) => {
    // verificando se o token de depuracao foi fornecido.
    /*
    if (context.app == undefined) {
      throw new functions.https.HttpsError(
        "failed-precondition",
        "Erro ao acessar a function sem token do AppCheck.");
    }*/
    // inicializar um objeto padrao de resposta já com erro.
    // será modificado durante o curso.
    const cResponse: CustomResponse = {
      status: "ERROR",
      message: "Dados não fornecidos",
      payload: undefined,
    };
    // verificar se o objeto usuario foi fornecido
    const user = (data as User);
    if (hasAccountData(user)) {
      try {
        const doc = await firebase.firestore()
          .collection("users")
          .add(user);
        if (doc.id != undefined) {
          cResponse.status = "SUCCESS";
          cResponse.message = "Perfil de usuário inserido";
          cResponse.payload = JSON.stringify({docId: doc.id});
        } else {
          cResponse.status = "ERROR";
          cResponse.message = "Não foi possível inserir o perfil do usuário.";
          cResponse.payload = JSON.stringify({errorDetail: "doc.id"});
        }
      } catch (e) {
        let exMessage;
        if (e instanceof Error) {
          exMessage = e.message;
        }
        functions.logger.error("Erro ao incluir perfil:", user.email);
        functions.logger.error("Exception: ", exMessage);
        cResponse.status = "ERROR";
        cResponse.message = "Erro ao incluir usuário - Verificar Logs";
        cResponse.payload = null;
      }
    } else {
      cResponse.status = "ERROR";
      cResponse.message = "Perfil faltando informações";
      cResponse.payload = undefined;
    }
    return JSON.stringify(cResponse);
  });

export const updateUserProfile = functions
  .region("southamerica-east1")
  .runWith({enforceAppCheck: false})
  .https
  .onCall(async (data, context) => {
    const cResponse: CustomResponse = {
      status: "ERROR",
      message: "Dados não fornecidos",
      payload: undefined,
    };
    const user = (data as User);
    if (!user.uid) {
      cResponse.status = "ERROR";
      cResponse.message = "O objeto do usuário não tem um ID válido.";
      return JSON.stringify(cResponse);
    }
    try {
      const docRef = firebase.firestore().collection("users").doc(user.uid);
      await docRef.update(user);
      cResponse.status = "SUCCESS";
      cResponse.message = "Perfil de usuário atualizado";
      cResponse.payload = JSON.stringify({docId: docRef.id});
    } catch (e) {
      let exMessage;
      if (e instanceof Error) {
        exMessage = e.message;
      }
      functions.logger.error("Erro ao atualizar perfil:", user.email);
      functions.logger.error("Exception: ", exMessage);
      cResponse.status = "ERROR";
      cResponse.message = "Erro ao atualizar usuário - Verificar Logs";
      cResponse.payload = null;
    }
    return JSON.stringify(cResponse);
  });

export const getUserProfile = functions
  .region("southamerica-east1")
  .runWith({enforceAppCheck: false})
  .https
  .onCall(async (data, context) => {
    const cResponse: CustomResponse = {
      status: "ERROR",
      message: "Dados não fornecidos",
      payload: undefined,
    };
    const userId = data.uid as string;
    if (!userId) {
      cResponse.status = "ERROR";
      cResponse.message = "O ID do usuário não foi fornecido.";
      return JSON.stringify(cResponse);
    }
    try {
      const docRef = firebase.firestore().collection("users").doc(userId);
      const doc = await docRef.get();
      if (!doc.exists) {
        cResponse.status = "ERROR";
        cResponse.message = "O documento não existe.";
        return JSON.stringify(cResponse);
      }
      const user = doc.data() as User;
      cResponse.status = "SUCCESS";
      cResponse.message = "Dados do usuário recuperados";
      cResponse.payload = JSON.stringify(user);
    } catch (e) {
      let exMessage;
      if (e instanceof Error) {
        exMessage = e.message;
      }
      functions.logger.error("Erro ao recuperar perfil do usuário:", userId);
      functions.logger.error("Exception: ", exMessage);
      cResponse.status = "ERROR";
      cResponse.message = "Erro ao recuperar usuário - Verificar Logs";
      cResponse.payload = null;
    }
    return JSON.stringify(cResponse);
  });
