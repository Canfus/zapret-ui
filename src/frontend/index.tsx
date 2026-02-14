import { useEffect, useRef } from "react";

import { ServiceRegistry } from "@core/services/zapret/config";

export const App = () => {
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const testFlow = async () => {
      if (!ref.current) return;

      try {
        const githubService = ServiceRegistry.createGithubService();
        // const zipService = ServiceRegistry.getZipService();

        const release = await githubService.getLatestRelease();
        const zipResult = await githubService.downloadLatestAsset();

        ref.current.insertAdjacentHTML(
          "beforeend",
          JSON.stringify({
            release,
            asset: zipResult
          })
        );
      } catch (error) {
        ref.current.insertAdjacentHTML("beforeend", String(error));
      }
    };

    testFlow();
  }, []);

  return <div ref={ref}>Hello world</div>;
};
