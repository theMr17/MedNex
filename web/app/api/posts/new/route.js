import Post from "@/models/post";
import { connectToDB } from "@/utils/database";

export const POST = async (request) => {
    const { text, author, createdAt, parentId, children } =
        await request.json();

    try {
        await connectToDB();
        const newPost = new Post({
            text: text,
            author: author,
            createdAt: createdAt,
            parentId: parentId,
            children: children,
        });

        await newPost.save();
        return new Response(JSON.stringify(newPost), { status: 201 });
    } catch (error) {
        return new Response("Failed to create a new post\n" + error, {
            status: 500,
        });
    }
};