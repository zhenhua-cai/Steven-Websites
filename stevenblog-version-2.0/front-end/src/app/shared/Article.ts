export class Article {
  id: string;
  title: string;
  content: string;
  summary: string;
  username: string;
  createDate: Date;
  lastModified: Date;

  constructor(id: string, title: string, content: string, username: string, createDate: Date, lastModified: Date, summary: string = null) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.username = username;
    this.createDate = createDate;
    this.lastModified = lastModified;
    this.summary = summary;
  }
}
