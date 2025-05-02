const { execSync, spawn } = require('child_process');
const fs = require('fs');
const path = require('path');

const EMULATOR_NAME = 'Pixel_9_API_35_x86_64'; 

function isEmulatorRunning() {
  try {
    const output = execSync('adb devices').toString();
    return output.includes('emulator');
  } catch (error) {
    console.error('Error al verificar emuladores:', error);
    return false;
  }
}

function startEmulator() {
  console.log(`Iniciando emulador ${EMULATOR_NAME}...`);
  const emulatorPath = path.join(process.env.ANDROID_HOME || '', 'emulator', 'emulator.exe');
  const emulatorProcess = spawn(emulatorPath, ['-avd', EMULATOR_NAME], {
    detached: true,
    stdio: 'ignore'
  });
  
  emulatorProcess.unref();
  
  // Esperar a que el emulador este completamente iniciado
  let attempts = 0;
  const maxAttempts = 30;
  
  const checkEmulatorInterval = setInterval(() => {
    attempts++;
    
    if (isEmulatorRunning()) {
      console.log('Emulador listo!');
      clearInterval(checkEmulatorInterval);
      startReactNative();
    } else if (attempts >= maxAttempts) {
      console.error('Error: El emulador no se inicio correctamente despues de varios intentos.');
      clearInterval(checkEmulatorInterval);
      process.exit(1);
    } else {
      console.log(`Esperando a que el emulador arranque... (${attempts}/${maxAttempts})`);
    }
  }, 2000);
}

// Funcion para iniciar React Native
function startReactNative() {
  console.log('Iniciando la aplicacion React Native...');
  const reactProcess = spawn('npx', ['react-native', 'run-android'], {
    stdio: 'inherit'
  });
  
  reactProcess.on('close', (code) => {
    if (code != 0) {
      console.error(`La aplicacion React Native termina con codigo de error ${code}`);
      process.exit(code);
    }
    console.log('La aplicacion React Native se cerro correctamente');
  });
}

function main() {
  if (isEmulatorRunning()) {
    console.log('El emulador ya esta corriendo. Iniciando la aplicacion directamente...');
    startReactNative();
  } else {
    startEmulator();
  }
}

main();
