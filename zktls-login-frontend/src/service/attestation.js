
import { PrimusZKTLS } from "@primuslabs/zktls-js-sdk"
import {ethers} from "ethers";
// Initialize parameters.
const primusZKTLS = new PrimusZKTLS();

//**** Set appId and appSecret here!!!
const appId = "0x17ae11d76b72792478d7b7bcdc76da9574ab3cf8";
const appSecret =
  "0xafa01caf44f07d2b21bc5e2bde1de2a8ba56f33ac2e223169f99634f57d049b5";
if (!appId || !appSecret) {
  alert("appId or appSecret is not set.")
  throw new Error("appId or appSecret is not set.");
}
//just for test
const options = {
  env: "production"
}
primusZKTLS.init(appId, appSecret, options).then(() => {
  console.log("primusProof init success!");
});

export async function primusProofTest(templateId, callback) {
  // Set TemplateID and user address.
  const attTemplateID = templateId;
  // Create a wallet
  const wallet = ethers.Wallet.createRandom();

  // ***You change address according to your needs.***
  const userAddress = wallet.address;
  // Generate attestation request.
  const request = primusZKTLS.generateRequestParams(attTemplateID, userAddress);

  // Transfer request object to string.
  const requestStr = request.toJsonString();
  // Sign request.
  const signedRequestStr = await primusZKTLS.sign(requestStr);
  // Start attestation process.
  const attestation = await primusZKTLS.startAttestation(signedRequestStr);
  console.log("attestation=", attestation);

  // Verify siganture.
  const verifyResult = await primusZKTLS.verifyAttestation(attestation)
  console.log("verifyResult=", verifyResult);

  if (verifyResult === true) {
    // Business logic checks, such as attestation content and timestamp checks
    // do your own business logic.
    callback(attestation)
  } else {
    // If failed, define your own logic.
  }
}