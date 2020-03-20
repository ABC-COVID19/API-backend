import { IDocument } from 'app/shared/model/document.model';

export interface IRevision {
  id?: number;
  title?: string;
  summary?: string;
  reviewer?: string;
  document?: IDocument;
}

export class Revision implements IRevision {
  constructor(public id?: number, public title?: string, public summary?: string, public reviewer?: string, public document?: IDocument) {}
}
