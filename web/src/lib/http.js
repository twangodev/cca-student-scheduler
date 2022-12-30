import { PUBLIC_API_URL } from '$env/static/public';

export async function GET(url) {
	return await fetch(PUBLIC_API_URL + url);
}

export async function POST(url, data) {
	return await fetch(PUBLIC_API_URL + url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data)
	});
}

export async function PATCH(url, data) {
	return await fetch(PUBLIC_API_URL + url, {
		method: 'PATCH',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data)
	});
}

export async function DELETE(url) {
	return await fetch(PUBLIC_API_URL + url, {
		method: 'DELETE'
	});
}
