export class Article {
  id: string;
  title: string;
  content: string;
  summary: string;
  username: string;
  createDate: Date;
  lastModified: Date;
  privateMode: boolean;

  constructor(id: string,
              title: string,
              content: string,
              username: string,
              createDate: Date,
              lastModified: Date,
              summary: string = null,
              privateMode: boolean) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.username = username;
    this.createDate = createDate;
    this.lastModified = lastModified;
    this.summary = summary;
    this.privateMode = privateMode;
  }
}
