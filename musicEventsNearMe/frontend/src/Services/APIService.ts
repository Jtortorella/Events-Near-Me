interface APIRequestProps {
    url: string; //Always will need a URL to make the request.
    //May need to add apikey to url.
    apiKey?: string; //API Key is optional, some requests will not require an api key (i.e. working with the backend).
    body?: any; //Only for post / put / patch requests not needed for get or delete requests.
    options?: {
        "Content-Type": "application/json";
        "Authorization": string;
    };
}

async function makeRequest(method: string, props: APIRequestProps, data?: any): Promise<any> {
    const requestOptions: RequestInit = {
        method,
        headers: {
            ...(props?.apiKey && { "x-api-key": props.apiKey }), //API KEY WILL BE ADDED TO THE URL?
        },
        body: data ? JSON.stringify(data) : undefined,
    }
    try {
        let response: any = await fetch(props.url, requestOptions);
        const responseData = await response.json();
        return responseData;
    } catch (err) {
        console.log(err);
    }
}

export async function post(props: APIRequestProps, data?: any) {
    let response = await makeRequest('POST', props, data);
    return response;
}

export async function get(props: APIRequestProps) {
    return makeRequest('GET', props);
}

export async function put(props: APIRequestProps, data?: any) {
    return makeRequest('PUT', props, data);
}

export async function patch(props: APIRequestProps, data?: any) {
    return makeRequest('PATCH', props, data);
}

export async function del(props: APIRequestProps) {
    return makeRequest('DELETE', props);
}

