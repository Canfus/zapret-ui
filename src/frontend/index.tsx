import { useState } from "react";

export const App = () => {
  const [state, setState] = useState<string | undefined>();

  const onClick = () => {
    window.electronAPI
      .installZapret()
      .then(() => {
        setState("installed");
      })
      .catch(() => {
        setState("error");
      });
  };

  return (
    <div>
      Hello world
      {state}
      <button onClick={onClick}>Test</button>
    </div>
  );
};
