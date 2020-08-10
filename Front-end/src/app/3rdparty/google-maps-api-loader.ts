declare global {
  interface Window { initGoogleMaps: any; }
}

export function loadAPI(config: MapsApiConfig){
  return new Promise((resolve, reject) => {

    let script = document.createElement('script');
    // script.src = 'https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&callback=initGoogleMaps';
    script.src = `https://maps.googleapis.com/maps/api/js?${getParams(config)}&callback=initGoogleMaps`;
    script.defer = true;
    script.async = true;
    script.onerror = err => reject(err);

    // Attach your callback function to the `window` object
    window.initGoogleMaps = () => resolve();

    // Append the 'script' element to 'head'
    document.head.appendChild(script);
  })
}

function getParams(config: MapsApiConfig){
  const queryParams: { [key: string]: string | string[] } = {
    key: config.apiKey,
    libraries: config.libraries,
    region: config.region
  };

  return Object.keys(queryParams)
    .filter((k: string) => queryParams[k] != null)
    .filter((k: string) => {
      // remove empty arrays
      return !Array.isArray(queryParams[k]) ||
        (Array.isArray(queryParams[k]) && queryParams[k].length > 0);
    }).map((k: string) => {
      // join arrays as comma seperated strings
      const i = queryParams[k];
      if (Array.isArray(i)) {
        return { key: k, value: i.join(',') };
      }
      return { key: k, value: queryParams[k] };
    }).map((entry: { key: string, value: string }) => {
      return `${entry.key}=${entry.value}`;
    }).join('&');
}

interface MapsApiConfig {
  apiKey: string,
  region: string,
  libraries?: string[]
}
