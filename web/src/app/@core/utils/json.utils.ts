export class JsonUtils {

    public static convertMapToJsonObject<T>(map: Map<string, T>): Map<string, T> {
        let jsonObject = new Map();
          map.forEach((value, key) => {
            jsonObject[key] = value
          });
        return jsonObject;
      }
}