import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRevision } from 'app/shared/model/revision.model';

type EntityResponseType = HttpResponse<IRevision>;
type EntityArrayResponseType = HttpResponse<IRevision[]>;

@Injectable({ providedIn: 'root' })
export class RevisionService {
  public resourceUrl = SERVER_API_URL + 'api/revisions';

  constructor(protected http: HttpClient) {}

  create(revision: IRevision): Observable<EntityResponseType> {
    return this.http.post<IRevision>(this.resourceUrl, revision, { observe: 'response' });
  }

  update(revision: IRevision): Observable<EntityResponseType> {
    return this.http.put<IRevision>(this.resourceUrl, revision, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRevision>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRevision[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
