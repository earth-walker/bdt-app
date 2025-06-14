import { createSignal } from "solid-js";
import GoogleLoginButton from "./components/GoogleLoginButton";
// import { signIn, signUp } from "./auth/auth";

export default function AuthForm() {
  const [email, setEmail] = createSignal("");
  const [password, setPassword] = createSignal("");
  const [error, setError] = createSignal("");
  const [mode, setMode] = createSignal("signin");
  const [isSigningIn, setIsSigningIn] = createSignal(false);
  const handleSubmit = () => {};
  // const handleSubmit = async (e) => {
  //   e.preventDefault();
  //   try {
  //     if (mode() === "signin") {
  //       await signIn(email(), password());
  //       alert("Logged in!");
  //     } else {
  //       await signUp(email(), password());
  //       alert("Signed up!");
  //     }
  //   } catch (err) {
  //     setError(err.message);
  //   }
  // };

  const onGoogleSignIn = () => {};

  return (
    <div className="w-full h-screen flex self-center place-content-center place-items-center">
      <div className="w-96 text-gray-600 space-y-5 p-4 shadow-xl border rounded-xl">
        <form className="flex flex-col" onSubmit={handleSubmit}>
          <div className="text-gray-800 text-xl font-semibold sm:text-2xl">
            Benefits Decision Tookit
          </div>
          <div className="pt-4 flex flex-col">
            <label className="text-sm text-gray-500 font-semibold" for="email">
              Email
            </label>
            <input
              className="w-100 border-1 border-gray-200 bg-gray-100 p-2"
              type="email"
              id="email"
              placeholder="Enter your email"
              value={email()}
              onInput={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className="pt-8 flex flex-col">
            <label
              className="text-sm text-gray-500 font-semibold"
              for="password"
            >
              Password
            </label>
            <input
              className="w-100 border-1 border-gray-200 bg-gray-100 p-2"
              type="password"
              placeholder="Password"
              id="password"
              value={password()}
              onInput={(e) => setPassword(e.target.value)}
            />
          </div>
          <button
            className="mt-4 w-100 p-2 bg-emerald-500 rounded text-white font-bold"
            type="submit"
          >
            {mode() === "signin" ? "Sign In" : "Sign Up"}
          </button>
          <div
            className="text-gray-600 pt-4 text-center"
            onClick={() => setMode(mode() === "signin" ? "signup" : "signin")}
          >
            {mode() === "signin"
              ? "Don't have an account? "
              : "Have an account? "}
            <span className="text-gray-700 font-bold hover:underline hover:cursor-pointer">
              {mode() === "signin" ? "Sign Up" : "Sign In"}
            </span>
          </div>
          {error() && <p style={{ color: "red" }}>{error()}</p>}
        </form>
        <div class="relative flex w-100 h-12 justify-center items-center">
          <hr class="absolute w-100 border-t border-gray-300" />
          <div class="absolute flex w-100 items-center justify-center text-center text-md font-bold text-gray-500">
            <span className="bg-white px-2">OR</span>
          </div>
        </div>
        <GoogleLoginButton
          isSigningIn={isSigningIn}
          onGoogleSignIn={onGoogleSignIn}
        ></GoogleLoginButton>
      </div>
    </div>
  );
}
