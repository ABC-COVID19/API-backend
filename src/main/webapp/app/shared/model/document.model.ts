import { Moment } from 'moment';
import { IRevision } from 'app/shared/model/revision.model';
import { ITopic } from 'app/shared/model/topic.model';

export interface IDocument {
  id?: number;
  title?: string;
  date?: Moment;
  abstractDesc?: string;
  keywords?: any;
  source?: string;
  link?: string;
  revisions?: IRevision[];
  topic?: ITopic;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public title?: string,
    public date?: Moment,
    public abstractDesc?: string,
    public keywords?: any,
    public source?: string,
    public link?: string,
    public revisions?: IRevision[],
    public topic?: ITopic
  ) {}
}
