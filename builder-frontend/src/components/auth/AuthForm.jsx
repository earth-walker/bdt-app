import { createSignal } from "solid-js";
import GoogleLoginButton from "../GoogleLoginButton";
import { useAuth } from "../../context/AuthContext";
import Login from "./Login";
import Signup from "./Signup";

const firebaseErrorMessages = {
  "auth/user-not-found": "No user found with this email.",
  "auth/wrong-password": "Incorrect password.",
  "auth/invalid-email": "Please enter a valid email address.",
  "auth/missing-password": "Please enter your password.",
  "auth/user-disabled": "This user account has been disabled.",
  "auth/too-many-requests": "Too many failed attempts. Please try again later.",
  "auth/weak-password": "Password must be at least 6 characters.",
  // "auth/invalid-credential": "Click sign Up to create a new account"
  // Add more as needed
};

function getFriendlyErrorMessage(firebaseError) {
  return (
    firebaseErrorMessages[firebaseError.code] ||
    "Login failed. Please try again."
  );
}

export default function AuthForm() {
  const [email, setEmail] = createSignal("");
  const [password, setPassword] = createSignal("");
  const [error, setError] = createSignal("");
  const [mode, setMode] = createSignal("signin");
  const [isSigningIn, setIsSigningIn] = createSignal(false);
  const { loginWithGoogle } = useAuth();

  const toggleMode = () => {
    if (mode() === "signin") {
      setMode("signup");
    } else {
      setMode("signin");
    }
  };

  const handleGoogleLogin = async () => {
    try {
      setIsSigningIn(true);
      await loginWithGoogle();
      setIsSigningIn(false);
    } catch (err) {
      setIsSigningIn(false);

      console.error(err.message);
    }
  };

  return (
    <div className="w-full h-screen flex self-center place-content-center place-items-center">
      <div className="w-96 text-gray-600 space-y-5 p-4 shadow-xl border rounded-xl">
        <div className="text-gray-800 text-xl font-semibold sm:text-2xl">
          Benefits Decision Tookit
        </div>
        {mode() === "signin" ? (
          <Login toggleMode={toggleMode}></Login>
        ) : (
          <Signup toggleMode={toggleMode}></Signup>
        )}
        <div class="relative flex w-100 h-12 justify-center items-center">
          <hr class="absolute w-100 border-t border-gray-300" />
          <div class="absolute flex w-100 items-center justify-center text-center text-md font-bold text-gray-500">
            <span className="bg-white px-2">OR</span>
          </div>
        </div>
        <GoogleLoginButton
          isSigningIn={isSigningIn}
          onGoogleSignIn={handleGoogleLogin}
        ></GoogleLoginButton>
      </div>
    </div>
  );
}
