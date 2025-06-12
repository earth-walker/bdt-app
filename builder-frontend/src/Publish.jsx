import { getSelectedProjectFromStorage } from "./storageUtils/storageUtils";

export default function Publish() {
  const { id, screenerName, isPublished, lastPublishDate } =
    getSelectedProjectFromStorage();

  const handlePublish = async () => {};
  return (
    <div className="py-8 flex justify-center">
      <div className="px-8 py-4 w-xl border-1 shadow-sm border-gray-200">
        <div className="text-xl">{screenerName}</div>
        <div className="mt-4 flex flex-col gap-2">
          <div className=" flex gap-4">
            <div className="font-bold">Screener URL:</div>
            {isPublished ? (
              <a>test-screener.app</a>
            ) : (
              <a>Deploy screener to create public url.</a>
            )}
          </div>
          <div className="flex gap-4">
            <div className="font-bold">Last Published Date:</div>
            {lastPublishDate ? (
              <div>{lastPublishDate}</div>
            ) : (
              <div>Not yet published</div>
            )}
          </div>
        </div>
        <div className="mt-4 flex flex-col gap-2">
          <button className="w-80 bg-gray-800 font-bold text-gray-50 rounded px-4 py-2 hover:bg-gray-700">
            Deploy Screener
          </button>
          {lastPublishDate ? (
            <div>
              Deploy the current working version to your public screener
            </div>
          ) : (
            <div>Click to make your screener availble through a public URL</div>
          )}
        </div>
      </div>
    </div>
  );
}
