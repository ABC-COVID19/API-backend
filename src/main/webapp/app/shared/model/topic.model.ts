import { IDocument } from 'app/shared/model/document.model';

export interface ITopic {
  id?: number;
  name?: string;
  names?: IDocument[];
}

export class Topic implements ITopic {
  constructor(public id?: number, public name?: string, public names?: IDocument[]) {}
}
