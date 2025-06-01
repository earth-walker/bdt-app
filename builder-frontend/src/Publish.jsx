export default function Publish() {
  return (
    <div className="py-8 flex justify-center">
      <div className="px-8 py-4 w-xl border-1 shadow-sm border-gray-200">
        <div className="text-xl">Screener Name</div>
        <div className="mt-4 flex flex-col gap-2">
          <div className=" flex gap-4">
            <div className="font-bold">Screener URL:</div>
            <a>test-screener.app</a>
          </div>
          <div className="flex gap-4">
            <div className="font-bold">Last Updated:</div>{" "}
            <div>01/21/2025 12:30PM</div>
          </div>
        </div>
        <div className="mt-4 flex flex-col gap-2">
          <button className="w-80 bg-gray-800 font-bold text-gray-200 rounded px-4 py-2 border-2 border-gray-600">
            Deploy Screener
          </button>
          <div>Deploy the current working version to your public screener</div>
        </div>
      </div>
    </div>
  );
}
