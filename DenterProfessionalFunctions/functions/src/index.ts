import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const collUsers = db.collection("users");

export const addSapleUser = functions
  .region("southamerica-east1")
  .https.onRequest(async (request, response) => {
    const user = {
      name: "Messi",
      phone: "(19)2333-1233",
      adress1: "Rua Russin Martins Molejo, 171",
      adress2: "",
      adress3: "",
      cep: "13140-540",
      // eslint-disable-next-line max-len
      miniResume: "Eu sou Messi Careca, dentista com 5 anos de experiência em diversas áreas da odontologia, formado pela Universidade Federal de Minas Gerais.",
    };
    try {
      const docRef = await collUsers.add(user);
      response.send("Usuario cadastrado com sucesso: " + docRef);
    } catch (e) {
      functions.logger.error("Erro");
      response.send("Erro ao inserir");
    }
  });

export const addUser = functions
  .https
  .onRequest(async (request, response) => {
    try {
      const collectionName = request.query.collection as string;
      const userData = request.body as Record<string, unknown>;
      const collectionRef = db.collection(collectionName);
      const docRef = await collectionRef.add(userData);
      response.status(201).send(`Usuário adicionado com sucesso: ${docRef.id}`);
    } catch (error) {
      console.error(error);
      response.status(500).send("Erro ao adicionar usuário");
    }
  });
